import ballerina/test;

@test:Config { }
function testFunction() returns error? {
    string[] result = check listEntries("src/zip/tests/resources/test.zip");
    test:assertEquals(result.length(), 2);
}
