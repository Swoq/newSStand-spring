package com.swoqe.newsstand.util;

public enum OrderBy {
	title,
	publicationDate;

	public static OrderBy safeValueOf(final String value) {
		try {
			return OrderBy.valueOf(value);
		} catch (IllegalArgumentException | NullPointerException e) {
			return title;
		}
	}
}