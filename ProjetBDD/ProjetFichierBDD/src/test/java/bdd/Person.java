package bdd;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Jason Mahdjoub
 * @version 1.0
 */
public class Person implements Serializable {
	private String firstName;
	private String lastName;
	private int age;

	public Person() {
		firstName="firstName"+((int)(10000*Math.random()));
		lastName="lastName"+((int)(10000*Math.random()));
		age=((int)(10000*Math.random()));
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return age == person.age &&
				firstName.equals(person.firstName) &&
				lastName.equals(person.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, age);
	}

	@Override
	public String toString() {
		return "Person{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", age=" + age +
				'}';
	}
}
