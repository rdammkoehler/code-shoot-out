package com.noradltd.shootout.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class RowMatcher extends TypeSafeMatcher<ResultSet> {

	private String firstName, lastName;

	public RowMatcher(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean matchesSafely(ResultSet resultSet) {
		try {
			boolean returnValue = false;
			returnValue = resultSet.next();
			assertThat(resultSet.getString(1), is(firstName));
			assertThat(resultSet.getString(2), is(lastName));
			return returnValue;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void describeTo(Description description) {
		description.appendText("is not " + firstName + " " + lastName);
	}

	public void describeMismatch(ResultSet resultSet, Description description) {
		description.appendText("is not " + firstName + " " + lastName);
	}

	@Factory
	public static <T> Matcher<ResultSet> contains(String firstName, String lastName) {
		return new RowMatcher(firstName, lastName);
	}

}