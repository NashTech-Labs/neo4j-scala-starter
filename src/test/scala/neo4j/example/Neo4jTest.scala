package neo4j.exemple

import neo4j.example.Neo4j
import neo4j.example.Neo4j.User
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import org.scalatest.{Sequential, BeforeAndAfterEach, FunSuite}


class Neo4jTest extends FunSuite with BeforeAndAfterEach {

  override def beforeEach() {
    val driver = GraphDatabase.driver("bolt://localhost/7687", AuthTokens.basic("anurag", "@nurag06"))
    val session = driver.session()
    session.run("CREATE (user:Users {name:'Anurag',last_name:'Srivastava',age:26,city:'delhi'})")
    session.close()
  }

  override def afterEach() {
    val driver = GraphDatabase.driver("bolt://localhost/7687", AuthTokens.basic("anurag", "@nurag06"))
    val session = driver.session()
    session.run("MATCH (n) OPTIONAL MATCH (n)-[r]-() WITH n,r LIMIT 50000 DELETE n,r")
    session.close()
  }

  Sequential

  test("insert data into neo4j") {
    val user = User("Shivansh", "srivastava", 22, "delhi")
    val res = Neo4j.insertRecord(user)
    assert(res == 1)
  }

  test("retrieve data from neo4j") {
    val user = User("Gaurav", "srivastava", 22, "delhi")
    Neo4j.insertRecord(user)
    val res = Neo4j.retrieveRecord("Gaurav")
    assert(res == "Gaurav")
  }

  test("retrieve data that not present in neo4j") {
    val res = Neo4j.retrieveRecord("Gaurav")
    assert(res == "Gaurav not found.")
  }

  test("retrieve all data from neo4j") {
    /*val user = User("Gaurav", "srivastava", 22, "delhi")
    Neo4j.insertRecord(user)*/
    val res = Neo4j.retrieveAllRecord()
    assert(res)
  }

  test("Not find data while retrieving all data from neo4j") {
    val driver = GraphDatabase.driver("bolt://localhost/7687", AuthTokens.basic("anurag", "@nurag06"))
    val session = driver.session()
    session.run("MATCH (n) OPTIONAL MATCH (n)-[r]-() WITH n,r LIMIT 50000 DELETE n,r")
    val res = Neo4j.retrieveAllRecord()
    assert(!res)
  }

  test("update data from neo4j") {
    val user = User("Gaurav", "srivastava", 22, "delhi")
    Neo4j.insertRecord(user)
    val res = Neo4j.updateRecord("Gaurav", "vicky")
    assert(res)
  }

  test("unable to update data in neo4j") {
    val res = Neo4j.updateRecord("Gaurav", "vicky")
    assert(!res)
  }

  test("delete data from neo4j") {
    val res = Neo4j.deleteRecord("Anurag")
    assert(res == 1)
  }

  test("create Node's relation") {
    val user_shivansh = User("Shivansh", "srivastava", 22, "delhi")
    Neo4j.insertRecord(user_shivansh)
    val user_manish = User("manish", "Mishra", 27, "delhi")
    Neo4j.insertRecord(user_manish)
    val user_sandy = User("Sandy", "sandeep", 26, "delhi")
    Neo4j.insertRecord(user_sandy)
    val res = Neo4j.createNodesWithRelation("Anurag", List("Shivansh", "Sandy", "Manish"), "Friends")
    assert(res == 3)
  }

  test("Friends of friends data from neo4j") {
    val user_shivansh = User("Shivansh", "srivastava", 22, "delhi")
    Neo4j.insertRecord(user_shivansh)
    val user_manish = User("manish", "Mishra", 27, "delhi")
    Neo4j.insertRecord(user_manish)
    val user_sandy = User("Sandy", "sandeep", 26, "delhi")
    Neo4j.insertRecord(user_sandy)
    val user_akash = User("Akash", "srivastava", 20, "delhi")
    Neo4j.insertRecord(user_akash)
    Neo4j.createNodesWithRelation("Anurag", List("Shivansh", "Sandy", "Manish"), "Friends")
    Neo4j.createNodesWithRelation("Sandy", List("akash"), "Friends")
    val res = Neo4j.fetchFriendsOfFriends("Anurag", "Friends")
    assert(res == "akash")
  }
}
