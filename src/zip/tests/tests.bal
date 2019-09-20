import ballerina/test;

@test:Config { }
function testListEntries() returns error? {
    string[] result = check listEntries("src/zip/tests/resources/test.zip");
    test:assertEquals(result.length(), 6);
}

@test:Config { }
function testZipUnzip() returns error? {
    check unzip("src/zip/tests/resources/test.zip", "target/test-tmp/dir1/");
    check zip("target/test-tmp/dir1", "target/test-tmp/out.zip");
    string[] result = check listEntries("target/test-tmp/out.zip");
    test:assertEquals(result.length(), 6);
}
