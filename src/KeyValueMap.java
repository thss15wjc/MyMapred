import java.util.*;
import java.util.Map.Entry;

public class KeyValueMap<K,V> {
	  
	  public Map<K, V> map = new HashMap<K, V>();
	  public Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
	  public void sort() {
		  if (map == null) return;
		  List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		  Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
		    @Override
		    public int compare(Map.Entry<K, V> mapping1,Map.Entry<K, V> mapping2) {
		    	String str1 = (String) mapping1.getKey();
		    	String str2 = (String) mapping2.getKey();
		    	return str1.compareTo(str2);
		    }
		});
		
	  }
	  public Set<K> getKeySet() {
		    return map.keySet();
		  }
	  
	  public Collection<V> getValues() {
		    return map.values();
		  }
	  public V GetValueFromKey(Integer key) {   
		      return map.get(key);		
		  }
	  public K nextKey() {
		    if (it.hasNext()) {
		      return it.next().getKey();
		    }
			return null;
		  }
	  public V nextValue() {
		    if (it.hasNext()) {
		      return it.next().getValue();
		    }
			return null;
		  }
	}
