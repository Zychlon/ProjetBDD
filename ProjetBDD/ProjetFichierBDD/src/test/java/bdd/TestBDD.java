package bdd;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason Mahdjoub
 * @version 1.0
 */
public class TestBDD {

	private static final File bddFile=new File("bddFileTest.toRemove");
	private BDD bdd=null;


	@Test
	public void testNull() throws IOException, ClassNotFoundException {
		checkBddOpened();
		try {
			bdd.putObject(null, new Person());
			Assert.fail("NullPointerException should be generated");
		}
		catch (NullPointerException ignored)
		{

		}
		try {
			bdd.getObject(null);
			Assert.fail("NullPointerException should be generated");
		}
		catch (NullPointerException ignored)
		{

		}
		try {
			bdd.removeObject(null);
			Assert.fail("NullPointerException should be generated");
		}
		catch (NullPointerException ignored)
		{

		}
	}

	@Test(invocationCount = 10, threadPoolSize = 1, dependsOnMethods = "testNull")
	public void testAddGetAndRemoveWithoutClosing() throws Exception {
		testAddGetAndRemove(false);
	}

	@Test(invocationCount = 10, threadPoolSize = 1, dependsOnMethods = "testAddGetAndRemoveWithoutClosing")
	public void testAddGetAndRemoveWithClosing() throws Exception {
		testAddGetAndRemove(true);
	}

	@Test(dependsOnMethods = "testAddGetAndRemoveWithClosing")
	public void randomTests() throws Exception {
		checkBddOpened();
		HashMap<String, Person> persons=new HashMap<>();
		int keyID=0;
		for (int i=0;i<1000;i++)
		{
			if (Math.random()<0.4 && persons.size()>0)
			{
				Map.Entry<String, Person> e=persons.entrySet().stream().findAny().get();
				Person p2=(Person)bdd.getObject(e.getKey());
				Assert.assertEquals(p2, e.getValue());
				Assert.assertTrue(bdd.removeObject(e.getKey()), "When the object is removed, the function must return true");
				Assert.assertFalse(bdd.removeObject(e.getKey()), "When the object does not exist, the function must return false");
				Assert.assertNotNull(persons.remove(e.getKey()));
			}
			else
			{
				Person p=new Person();
				String name="name"+(keyID++);
				persons.put(name, p);
				bdd.putObject(name, p);
			}
			for (Map.Entry<String, Person> e : persons.entrySet())
			{
				Person p2=(Person)bdd.getObject(e.getKey());
				Assert.assertNotNull(p2, "The person "+e.getKey()+" should be present into the database");
				Assert.assertEquals(p2, e.getValue());
			}
			if (Math.random()<0.1)
				closeAndOpen();
		}
	}

	private void closeAndOpen() throws Exception {
		HashMap<String, Long> links=bdd.getLinks();
		checkClose();
		checkBddOpened();
		Assert.assertEquals(bdd.getLinks(), links);
	}

	private void testAddGetAndRemove(boolean closeFile) throws Exception {
		checkBddOpened();
		Person p=new Person();
		String name="ObjectToRemove";
		Assert.assertNull(bdd.getObject(name), "The object "+name+" should not be present into the database");
		bdd.putObject(name, p);
		if (closeFile)
		{
			closeAndOpen();
		}
		System.out.println(bdd.getLinks());
		Person np=(Person)bdd.getObject(name);
		Assert.assertEquals(np, p, "Add/get object does not work");
		Assert.assertTrue(bdd.removeObject(name), "When the object is removed, the function must return true");
		if (closeFile)
		{
			closeAndOpen();
		}
		Assert.assertFalse(bdd.removeObject(name), "When the object does not exist, the function must return false");
		if (closeFile)
		{
			closeAndOpen();
		}
		Assert.assertNull(bdd.getObject(name), "The object should be removed from the database");


	}

	private void checkBddOpened() throws IOException, ClassNotFoundException {
		if (bdd==null)
			bdd=new BDD(bddFile);
	}

	@AfterTest
	private void checkClose() throws Exception {
		if (bdd!=null) {
			bdd.close();
			bdd=null;
		}
	}

	@AfterClass
	public static void removeFile()
	{
		//noinspection ResultOfMethodCallIgnored
		bddFile.delete();
	}

}
