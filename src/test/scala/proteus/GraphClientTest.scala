package proteus

import com.cornfluence.proteus.{DocumentClient, GraphClient, User}
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.duration._

class GraphClientTest extends FunSpec {

   val testDB = "testGraphClient"
   val testEdgeCollection = "testEdgeClientCollection"
   val testVertexCollection = "testVertexClientCollection"
   var testDocID = ""
   val driver = new GraphClient(databaseName = testDB)

   var fromID = ""


   describe("==============\n| Graph Client Test |\n==============") {
      describe("Create Database") {
         it("should create new Database") {
            val result = driver.createDatabase(testDB, Some(List(User("graphUser", "password"))))
            result.onComplete {
               case Success(res) => res.right.get shouldEqual "success"
               case Failure(t) => fail(t)
            }
         }
      }

      describe("Create Edge") {
         it("should create edge in test collection") {
            Thread.sleep(1000)
            val docDriver = new DocumentClient(databaseName = testDB)

            val result1 = docDriver.createDocument(testDB, testVertexCollection, """{ "Hello": "World" }""")
            val res1 = Await.result(result1, 5 second)
            fromID = res1.right.get

            val result2 = docDriver.createDocument(testDB, testVertexCollection, """{ "Hello": "World" }""")
            val res2 = Await.result(result2, 5 second)
            val toID = res2.right.get

            val result3 = driver.createEdge(testDB, testEdgeCollection, """{ "Hello": "World" }""", testVertexCollection, testVertexCollection, fromID, toID)

            val res3 = Await.result(result3, 5 second)
            testDocID = res3.right.get

            result3.onComplete {
               case Success(res) => res.right.get.toLong should be > 0L;
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Retrieve All Edges") {
         it("should retrieve all edges in test collection") {
            val result = driver.getAllEdges(testDB, testEdgeCollection, s"$testVertexCollection/$fromID")

            result.onComplete {
               case Success(res) => res should include(testVertexCollection)
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Retrieve one edge by handle") {
         it("should retrieve one edge from the test collection") {
            val result = driver.getEdge(testDB, testEdgeCollection, testDocID)

            result.onComplete {
               case Success(res) => res should include( s"""{"Hello":"World","_id":"$testEdgeCollection/""" + testDocID)
               case Failure(t) => fail(t)
            }
         }
      }
//      describe("Replace one edge by handle") {
//         it("should replace one edge from the test collection") {
//
//            val driver = new GraphClient(databaseName = testDB)
//            val result = driver.replaceEdge(testDB, testEdgeCollection, testDocID, """{ "Hello": "Arango" }""")
//
//            result.onComplete {
//               case Success(res) => res.right.get should include(testDocID)
//               case Failure(t) => fail(t)
//            }
//         }
//      }
//      describe("Ensure replaced edge has changed") {
//         it("replaced edge should have changed in the test collection") {
//
//            val driver = new GraphClient(databaseName = testDB)
//            val result = driver.getEdge(testDB, testEdgeCollection, testDocID)
//
//            result.onComplete {
//               case Success(res) => res should include( s"""{"Hello":"Arango","_id":"$testEdgeCollection/""" + testDocID)
//               case Failure(t) => fail(t)
//            }
//         }
//      }
      describe("Remove a edge by handle") {
         it("should remove one edge from the test collection") {
            val result = driver.deleteEdge(testDB, testEdgeCollection, testDocID)

            result.onComplete {
               case Success(res) => res.right.get should include("success")
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Delete Database") {
         it("should delete Database") {
            val result = driver.deleteDatabase(testDB)
            result.onComplete {
               case Success(res) => res.right.get should include("success")
               case Failure(t) => fail()
            }
         }
      }
   }
}

