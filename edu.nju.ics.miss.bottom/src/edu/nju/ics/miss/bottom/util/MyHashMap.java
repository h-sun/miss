package edu.nju.ics.miss.bottom.util;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> {

	private List<K> keyList = new ArrayList<K>();

	private List<V> valueList = new ArrayList<V>();

	public MyHashMap() {
	}

	public void put(K key, V value) {
		if (key == null || value == null) {
			return;
		}

		int index = keyList.indexOf(key);
		if (index < 0) {
			keyList.add(key);
			valueList.add(value);
		} else {
			valueList.set(index, value);
		}
	}

	public V get(K key) {
		if (key == null) {
			return null;
		}

		int index = keyList.indexOf(key);
		if (index < 0) {
			return null;
		}

		return valueList.get(index);
	}

	public List<K> keySet() {
		return keyList;
	}

	public List<V> valueSet() {
		return valueList;
	}

	public boolean containsKey(K key) {
		if (key == null) {
			return false;
		}
		if (keyList.contains(key)) {
			return true;
		}
		return false;
	}

}
