package com.hit.games;

class MutableInteger
{
	private int value;
	
	MutableInteger(int v) {
		value = v;
	}

	int getValue() {
		return value;
	}

	void setValue(int value) {
		this.value = value;
	}
	
	void increase() {
		value++;
	}	
}