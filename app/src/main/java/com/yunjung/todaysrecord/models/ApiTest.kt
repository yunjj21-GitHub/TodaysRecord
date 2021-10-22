package com.yunjung.todaysrecord.models

data class ApiTest (
    var results : List<Results>,
    var info : Info
)

data class Results(
    var gender : String,
    var name : Name,
    var location : LocationTest,
    var email : String,
    var login : Login,
    var dob : Dob,
    var registered : Registered,
    var phone : String,
    var cell : String,
    var id : ID,
    var picture : Picture,
    var nat : String
)

data class Info(
    var seed : String,
    var results : Int,
    var page : Int,
    var version : String
)

data class Name(
    var title : String,
    var first : String,
    var last : String
)

data class LocationTest(
    var street : Street,
    var city : String,
    var state : String,
    var country : String,
    var postcode : Int,
    var coordinates : Coordinates,
    var timezone : Timezone
)

data class Street(
    var number : Int,
    var name : String
)

data class Coordinates(
    var latitude : String,
    var longitude: String
)

data class Timezone(
    var offset : String,
    var description : String
)

data class Login(
    var uuid : String,
    var username : String,
    var password : String,
    var salt : String,
    var md5 : String,
    var sha1 : String,
    var sha256 : String
)

data class Dob(
    var date : String,
    var age : Int
)

data class Registered(
    var date : String,
    var age : Int
)

data class ID(
    var name : String,
    var value : String
)

data class Picture(
    var large : String,
    var medium : String,
    var thumbnail : String
)