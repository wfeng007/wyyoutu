/**
 * 
 */
package summ.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;

/**
 * @author wfeng007
 * @date 2011-11-18 上午10:52:17
 */
public class QueryStringParser {
    /**
     * 索引
     */
    private int index;
    /**
     * 总长
     */
    private int length;
    /**
     * QueryString
     */
    private String queryString;
    /**
     * # 与其后的内容
     */
    private String hash;
    /**
     * 字段列表
     */
    private Collection<Entry<String, String>> fieldList;

    /**
     * 使用默认字符集解码
     * @param queryString
     */
    public QueryStringParser(String queryString) {
        this(queryString, "utf-8");
    }

    /**
     * 使用指定字符集解码
     * @param queryString
     * @param charset
     */
    public QueryStringParser(String queryString, String charset) {
        this.queryString = queryString;
        fieldList = new ArrayList<Entry<String, String>>();
        parse(charset);
    }

    /**
     * 进入分析
     * @param charset
     */
    private void parse(String charset) {
//    	System.out.println("bg:parse");
        length = queryString.length();
        char ch;
        String key;
        String value;
        index = 0;
        do {
            if (index >= length) {
                break;
            }
            ch = queryString.charAt(index);
            if (ch == '&' || (index == 0 && ch != '#')) {
                if (index == 0 && ch != '?' && ch != '&') {
                    index--;
                }
                key = parseKey();
                /**
                 * fixedbug: key=value&key 异常
                 */
                if (index >= length) {
                    break;
                }
                ch = queryString.charAt(index);
                if (ch != '=') {
                    break;
                }
                value = parseValue();
//                System.out.println("getV:"+value);
                /**
                 * fixed: key decode
                 */
                try {
					fieldList.add(new FieldEntry(URIUtil.decode(key, charset), URIUtil.decode(value, charset)));
				} catch (URIException e) {
					e.printStackTrace();
				}
            } else if (ch == '#') {
                this.hash = parseHash();
            } else {
                break;
            }
        } while (true);

    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
    	QueryStringParser qsh=new QueryStringParser("?ddd=fds%20fds&fda=fdajkfjda%E4%B8%AD%E6%96%87","utf-8");
    	Map<String,String> mp=qsh.getMap();
    	System.out.println(mp);
	}
    
    /**
     * 内部
     * @author wfeng007
     * @date 2011-11-18 上午11:00:20
     */
    private static class FieldEntry implements Entry<String,String>{
    	
    	String key;
    	String value;
    	public FieldEntry(String key,String value){
    		this.key=key;
    		this.value=value;
    	}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#getKey()
		 */
		@Override
		public String getKey() {
			return this.key;
		}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#getValue()
		 */
		@Override
		public String getValue() {
			// TODO Auto-generated method stub
			return this.value;
		}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		@Override
		public String setValue(String value) {
			String oldValue=this.value;
			this.value=value;
			return oldValue;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return (key==null   ? 0 : key.hashCode()) ^  
				(value==null ? 0 : value.hashCode()); 
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
//			if(obj instanceof FieldEntry){
//				FieldEntry e=(FieldEntry) obj;
//				if (this.key != null && this.value != null
//						&& e.getKey() != null && e.getValue() != null
//						&& e.getKey().equals(this.key)
//						&& e.getValue().equals(this.value)) {
//					return true;
//				}
//			}
//			return false;
			
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;

		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getKey() + "=" + getValue();
		}
		
    }

    /**
     * 析出键
     * @return
     */
    private String parseKey() {
        StringBuilder strBuilder = new StringBuilder();
        char ch;
        index++;
        do {
            if (index >= length) {
                break;
            }
            ch = queryString.charAt(index);
            if (ch == '=') {
                break;
            }
            strBuilder.append(ch);
            index++;
        } while (true);
        return strBuilder.toString();
    }

    /**
     * 析出值
     * @return
     */
    private String parseValue() {
        StringBuilder strBuilder = new StringBuilder();
        char ch;
        index++;
        do {
            if (index >= length) {
                break;
            }
            ch = queryString.charAt(index);
            if (ch == '&' || ch == '#') {
                break;
            }
            strBuilder.append(ch);
            index++;
        } while (true);
        return strBuilder.toString();
    }

    /**
     * 析出 hash
     * @return
     */
    private String parseHash() {
        StringBuilder strBuilder = new StringBuilder();
        char ch;
        do {
            if (index >= length) {
                break;
            }
            ch = queryString.charAt(index);
            strBuilder.append(ch);
            index++;
        } while (true);
        return strBuilder.toString();
    }

    /**
     * 获取 hash (#与其后的字符)
     * @return
     */
    public String getHash() {
        return hash;
    }

    /**
     * 获取字段列表
     * @return
     */
    public Collection<Entry<String, String>> getFieldList() {
        return fieldList;
    }

    /**
     * <pre>
     * 获取Map
     * 注意：重复字段不会被覆盖，即重复字段会被忽略
     * </pre>
     * @return
     */
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<String, String> field : fieldList) {
            if (map.containsKey(field.getKey())) {
                continue;
            }
            map.put(field.getKey(), field.getValue());
        }
        return map;
    }

    /**
     * <pre>
     * 获取Map
     * 注意：重复字段不会被覆盖，即重复字段会被忽略
     * </pre>
     * @param str
     * @see #getMap()
     * @return
     */
    public static Map<String, String> toMap(String str) {
        QueryStringParser queryStringParser = new QueryStringParser(str);
        return queryStringParser.getMap();
    }

    /**
     * 获取字段列表
     * @param str
     * @see #getFieldList() 
     * @return
     */
    public static Collection<Entry<String, String>> toFieldList(String str) {
        QueryStringParser queryStringParser = new QueryStringParser(str);
        return queryStringParser.getFieldList();
    }

    /**
     * <pre>
     * 获取Map
     * 注意：重复字段不会被覆盖，即重复字段会被忽略
     * </pre>
     * @param str
     * @param charset
     * @see #getMap()
     * @return
     */
    public static Map<String, String> toMap(String str, String charset) {
        QueryStringParser queryStringParser = new QueryStringParser(str, charset);
        return queryStringParser.getMap();
    }

    /**
     * 获取字段列表
     * @param str
     * @param charset
     * @see #getFieldList()
     * @return
     */
    public static Collection<Entry<String, String>> getEntryList(String str, String charset) {
        QueryStringParser queryStringParser = new QueryStringParser(str, charset);
        return queryStringParser.getFieldList();
    }

    /**
     * 获取字段列表
     * @param str
     * @see #getFieldList()
     * @return
     */
    public static Collection<Entry<String, String>> getEntryList(String str) {
        return getEntryList(str, "utf-8");
    }

}
