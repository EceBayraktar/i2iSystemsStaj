package com.example.demo;

import io.restassured.RestAssured;

public class TestCreateBook {
    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("Deneme Kitap");
        book.setAuthor("Yazar Adı");

        RestAssured.given()
            .contentType("application/json")
            .body(book)
        .when()
            .post("http://localhost:8085/api/books")
        .then()
            .statusCode(201);
        
        System.out.println("Kitap başarıyla eklendi!");
    }
}
