package com.noradltd.shootout.rest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsResultSetEmpty extends TypeSafeMatcher<ResultSet> {

	@Override
	public boolean matchesSafely(ResultSet resultSet) {
		try {
			return !resultSet.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void describeTo(Description description) {
		description.appendText("is not empty");
	}

	public void describeMismatch(ResultSet resultSet, Description description) {
		description.appendText("is not empty");
	}
	
	@Factory
	public static <T> Matcher<ResultSet> empty() {
		return new IsResultSetEmpty();
	}

}