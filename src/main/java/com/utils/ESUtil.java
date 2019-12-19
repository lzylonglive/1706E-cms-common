package com.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * ���漰���·���
	 * 
	 * @param elasticsearchTemplate
	 * @param id
	 * @param object
	 */
	public static void saveObject(ElasticsearchTemplate elasticsearchTemplate,String id,Object object) {
//		�������Զ���
		IndexQuery query = new IndexQueryBuilder().withId(id).withObject(object).build();
//		��������
		elasticsearchTemplate.index(query);
	}
	
	/**
	 * ����ɾ��
	 * 
	 * @param elasticsearchTemplate
	 * @param clazz
	 * @param ids
	 */
	public static void deleteObject(ElasticsearchTemplate elasticsearchTemplate,Class<?> clazz,Integer ids[]) {
		for (Integer id : ids) {
//			��������
			elasticsearchTemplate.delete(clazz,id+"");
		}
	}
	
	/**
	 * 
	 * @Title: selectById
	 * @Description: ����id��es�������в�ѯ����
	 * @param elasticsearchTemplate
	 * @param clazz
	 * @param id
	 * @return
	 * @return: Object
	 */
	public static Object selectById(ElasticsearchTemplate elasticsearchTemplate,Class<?> clazz,Integer id) {
		GetQuery query = new GetQuery();
		
		query.setId(id+"");
		
		return elasticsearchTemplate.queryForObject(query, clazz);
	}
	
	/**
	 * 
	 * @param elasticsearchTemplate ģ�����
	 * @param clazz	ʵ�����class����
	 * @param page	��ǰҳ����0��ʼ
	 * @param pageSize	ÿҳ������
	 * @param sortField	��������ֶν�������
	 * @param fieldNames	Ҫ�������ֶ���
	 * @param value	����Ҫ����������
	 * @return
	 */
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
														//�����Ϊ�գ���ת����Date����
														Date value_date = null;
														if(value.getClass() == Long.class) {
															//�����Long����
															value_date = new Date(Long.valueOf(value + ""));
															
														}else {
															//�����String����
															SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
															
															value_date = sdf.parse(value.toString());
														}
														
														// bug
														field.set(entity, value_date);
													}
												} else if (type.isEnum()){
													
													field.set(entity, Enum.valueOf((Class<Enum>) type, value.toString()));
													
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
	
	/**
	 * 
	 * @param elasticsearchTemplate ģ�����
	 * @param clazz	ʵ�����class����
	 * @param classes ʵ������ʵ�����͵ĳ�Ա���������class����
	 * @param page	��ǰҳ����0��ʼ
	 * @param pageSize	ÿҳ������
	 * @param sortField	��������ֶν�������
	 * @param fieldNames	Ҫ�������ֶ���
	 * @param value	����Ҫ����������
	 * @return
	 */
	public static AggregatedPage<?> selectObjects(ElasticsearchTemplate elasticsearchTemplate, Class<?> clazz,
			List<Class> classes,Integer page, Integer pageSize, String sortField, String fieldNames[], String value) {
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
			queryBuilder = QueryBuilders.multiMatchQuery(value, fieldNames);//.analyzer("ik_max_word");
	 

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
														
														//�����Ϊ�գ���ת����Date����
														
														Date value_date = null;
														if(value.getClass() == Long.class) {
															//�����Long����
															value_date = new Date(Long.valueOf(value + ""));
															
														}else {
															//�����String����
															SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
															
															value_date = sdf.parse(value.toString());
														}
														
														// bug
														field.set(entity, value_date);
														
													}
												} else if (type.isEnum()){
													//ö��
													field.set(entity, Enum.valueOf((Class<Enum>) type, value.toString()));
													
												} else if (classes.contains(type)){
													if(value != null) {
														
														//��ʵ�������ʵ����
														Object obj = getEntityObject(value, type ,classes);
												    
														//������ֵ
														field.set(entity, obj);
													}
												} else{
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

				
				
				//�ݹ鷽��������ʵ�������
				private Object getEntityObject(Object value, Class<?> type, List<Class> classes)
						throws InstantiationException, IllegalAccessException, ParseException {
					//ʵ����
					Object obj = type.newInstance();
					Map map = (HashMap)value;
					
					//��ȡ�����ֶ�
					Field[] fields2 = type.getDeclaredFields();
					for (Field field2 : fields2) {
						
						//�ų���̬�����ͳ���
					    int mod = field2.getModifiers();
					    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					        continue;
					    }
 
					    //��������
					    field2.setAccessible(true);
					    
					    //��map�л�ȡ��Ӧ���ֶε�ֵ
					    Object value2 = map.get(field2.getName());
					    
					    //��������Date����
					    Class<?> type2 = field2.getType();
					    if (type2 == Date.class) {
							if (value2 != null) {
								//�����Ϊ�գ���ת����Date����
								
								Date value2_date = null;
								if(value2.getClass() == Long.class) {
									//�����Long����
									value2_date = new Date(Long.valueOf(value2 + ""));
									
								}else {
									//�����String����
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									
									value2_date = sdf.parse(value2.toString());
								}
								
								// bug
								field2.set(obj, value2_date);
							}
						}else if (type2.isEnum()){
							//ö��
							field2.set(obj, Enum.valueOf((Class<Enum>) type2, value2.toString()));
							
						} else if (classes.contains(type2)){
							if(value2 != null) {
								
								//��ʵ�������ʵ����
								Object obj2 = getEntityObject(value2, type2 ,classes);
						    
								//������ֵ
								field2.set(obj, obj2);
							}
						} else {
							
							field2.set(obj, value2);
						}
					    
					}
					return obj;
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
