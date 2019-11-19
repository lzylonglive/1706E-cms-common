package com.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��11��18��
 */
public class ESUtil {

	private static Logger logger = Logger.getLogger(ESUtil.class);
	
	public static void saveObject(ElasticsearchTemplate elasticsearchTemplate,String id,Object object) {
//		�������Զ���
		IndexQuery query = new IndexQueryBuilder().withId(id).withObject(object).build();
//		��������
		elasticsearchTemplate.index(query);
	}
	
	public static void deleteObject(ElasticsearchTemplate elasticsearchTemplate,Class<?> clazz,Integer ids[]) {
		for (Integer id : ids) {
//			��������
			elasticsearchTemplate.delete(clazz,id+"");
		}
	}
	
	public static Object selectById(ElasticsearchTemplate elasticsearchTemplate,Class<?> clazz,Integer id) {
		GetQuery query = new GetQuery();
		
		query.setId(id+"");
		
		return elasticsearchTemplate.queryForObject(query, clazz);
	}
	
	public static AggregatedPage<?> selectObjects(ElasticsearchTemplate elasticsearchTemplate, Class<?> clazz,
			Integer page, Integer pageSize, String sortField, String fieldNames[], String value) {
		AggregatedPage<?> pageInfo = null;
		logger.info("����es�������ݿ�Ĳ�ѯ������ʼ������������������������������������������������");
		// ����Pageable����
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortField));
		// ��ѯ����
		SearchQuery query = null;
		// ��ѯ���������Ĺ�������
		QueryBuilder queryBuilder = null;

		if (value != null && !"".equals(value)) {
			// ����ƴ�ӵ�ǰ׺���׺
			String preTags = "<font color=\"red\">";
			String postTags = "</font>";

			// ���崴�������Ĺ������϶���
			HighlightBuilder.Field highlightFields[] = new HighlightBuilder.Field[fieldNames.length];

			for (int i = 0; i < fieldNames.length; i++) {
				// �������������
				highlightFields[i] = new HighlightBuilder.Field(fieldNames[i]).preTags(preTags).postTags(postTags);
			}

			// ����queryBuilder����
			//.matchQuery("title", value).boost(3f);
			queryBuilder = QueryBuilders.multiMatchQuery(value, fieldNames);//.analyzer("ik_smart");
	 

//			queryBuilder = QueryBuilders.matchQuery("title", value).boost(7f);
			query = new NativeSearchQueryBuilder().withQuery(queryBuilder).withHighlightFields(highlightFields)
					.withPageable(pageable).build();

			pageInfo = elasticsearchTemplate.queryForPage(query, clazz, new SearchResultMapper() {

				@Override
				public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

					List<T> content = new ArrayList<T>();
					long total = 0l;

					try {
						// ��ѯ���
						SearchHits hits = response.getHits();
						//��ȡ��������
						TimeValue took = response.getTook();
						long millis = took.millis();
						if (hits != null) {
							// ��ȡ�ܼ�¼��
							total = hits.getTotalHits();
							// ��ȡ�������
							SearchHit[] searchHits = hits.getHits();
							// �жϽ��
							if (searchHits != null && searchHits.length > 0) {
								// �������
								for (int i = 0; i < searchHits.length; i++) {
									// ����ֵ
									T entity = clazz.newInstance();

									// ��ȡ����Ľ��
									SearchHit searchHit = searchHits[i];
									
									// ��ȡ��������е��ֶ�
									Field[] fields = clazz.getDeclaredFields();

									// �����ֶζ���
									for (int k = 0; k < fields.length; k++) {
										// ��ȡ�ֶζ���
										Field field = fields[k];
										// ��������
										field.setAccessible(true);
										// �ֶ�����
										String fieldName = field.getName();
										if (!fieldName.equals("serialVersionUID")) {
											HighlightField highlightField = searchHit.getHighlightFields()
													.get(fieldName);
											if (highlightField != null) {
												// ���� ���� �õ� ��<font color='red'> </font>��������Χ�����ݲ���
												String value = highlightField.getFragments()[0].toString();
												// ע��һ�����Ƿ��� string����
												field.set(entity, value);
											} else {
												// ��ȡĳ���ֶζ�Ӧ�� valueֵ
												Object value = searchHit.getSourceAsMap().get(fieldName);
												System.out.println(value);
												// ��ȡ�ֶε�����
												Class<?> type = field.getType();
												if (type == Date.class) {
													if (value != null) {
														// bug
														field.set(entity, new Date(Long.valueOf(value + "")));
													}
												} else {
													field.set(entity, value);
												}
											}
										}
									}

									content.add(entity);
								}
							}
						}
						System.out.println("����ʱ��"+millis);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new AggregatedPageImpl<>(content, pageable, total);
				}
			});

		} else {
			// û�в�ѯ�����ĵ�ʱ�򣬻�ȡes�е�ȫ������ ��ҳ��ȡ
			query = new NativeSearchQueryBuilder().withPageable(pageable).build();
			pageInfo = elasticsearchTemplate.queryForPage(query, clazz);
		}

		logger.info("����es�������ݿ�Ĳ�ѯ��������������������������������������������������������");

		return pageInfo;
	}
	
}
