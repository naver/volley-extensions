package com.navercorp.volleyextensions.view;

interface Restorable<T> {
	void restore(T value);
	T save();
}
