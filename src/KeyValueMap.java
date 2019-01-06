import java.util.*;

public class KeyValueMap<K,V> {
	  
	  //public Map<K, V> map = new HashMap<K, V>();
	 // public Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
	  private List<K>  mkey;
	  private List<V>  allvalue;
      private List<List<V>>  mvaluelist;
      private List<K>  allkey;
      private int index = 0;
      private int indexall = 0;
 
      public KeyValueMap(){
          mkey = new ArrayList<K>();
          allkey = new ArrayList<K>();
          allvalue = new ArrayList<V>();
          mvaluelist = new ArrayList<List<V>>();
      }
      public void nextMap() {
    	  indexall++;
      }
      public void nextReduce() {
    	  indexall++;
      }
      
      public boolean hasNextMap() {
    	 if (indexall < getMapSize()) return true; else return false;
      }
      
      public boolean hasNextReduce() {
     	 if (index < getReduceSize()) return true; else return false;
       }
      
	  public void putKeyValue(K key, V value) {
		  List list = new ArrayList<V>();
          list.add(value);
          allkey.add(key);
          allvalue.add(value);
          if (containsKey(key)){
              mvaluelist.get(mkey.indexOf(key)).add(value);
          }else {
               mkey.add(key);
               mvaluelist.add(list);
          }
	  }
	  
	  public K getkey(){
	        return mkey.get(index);
	    }
	 

	    public List<V> getvalues(){
	        return mvaluelist.get(index);
	    }

	    public K getSinglekey(){
	        return allkey.get(indexall);
	    }
	 
	    public V getSinglevalue(){
	        return allvalue.get(indexall);
	    }

	    public boolean containsKey(K key){
	          if (mkey.contains(key)){
	              return true;
	          }else {
	              return false;
	          }
	      }
	    public Map<K,List<V>> get(int i){
            Map<K,List<V>> map = new HashMap<>();
            map.put(mkey.get(i),mvaluelist.get(i));
           return map;
      }
//    /*
//     *获取全部元素
//     */
//     public Map<K,List<V>> getAll(){
//         Map<K,List<V>> map = new HashMap<>();
//         for (int i = 0; i < mkey.size(); i++) {
//             map.put(mkey.get(i),mvaluelist.get(i));
//         }
//         return map;
//     }
     public long getReduceSize(){
         return mkey.size();
     }
     public long getMapSize(){
         return allkey.size();
     }
//     public boolean removeAll(){
//         mkey.clear();
//         mvaluelist.clear();
//         if (mkey.isEmpty()&&mvaluelist.isEmpty()){
//             return true;
//         }else{
//             return false;
//         }
//     }

//	  public void sort() {
//		  if (mkey == null || mvaluelist == null) return;
//		  List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
//		  Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
//		    @Override
//		    public int compare(Map.Entry<K, V> mapping1,Map.Entry<K, V> mapping2) {
//		    	String str1 = (String) mapping1.getKey();
//		    	String str2 = (String) mapping2.getKey();
//		    	return str1.compareTo(str2);
//		    }
//		});
//		
//	  }
	  
//	  public K nextKey() {
//		    if (index < getReduceSize()) {
//		    	index++;
//		    	return mkey.get(index);
//		    }
//			return null;
//		  }
//
//
//	public List<V> nextValues() {
//		if (index < getReduceSize()) {
//	    	index++;
//	    	return mvaluelist.get(index);
//	    }
//			return null;
//		  }
	}
