import ballerinax/java;
import ballerinax/java.arrays;

function listEntriesExt(handle path) returns handle = @java:Method {
    name: "listEntries",
    class: "org.laf.libzip.ZipUtils"
} external;

function zipExt(handle sourceDir, handle targetZipFile) = @java:Method {
    name: "zip",
    class: "org.laf.libzip.ZipUtils"
} external;

function unzipExt(handle sourceZipFile, handle targetDir) = @java:Method {
    name: "unzip",
    class: "org.laf.libzip.ZipUtils"
} external;

public function listEntries(string path) returns string[]|error {
    string[] result = [];
    var entries = listEntriesExt(java:fromString(path));
    int count = arrays:getLength(entries);
    int i = 0;
    while i < count {
        var entry = java:toString(arrays:get(entries, i));
        if (entry is string) {
            result[i] = entry;
        } else {
            panic error("NULL entry in zip file");
        }
        i += 1;
    }
    return result;
}

public function zip(string sourceDir, string targetZipFile) returns error? {
    zipExt(java:fromString(sourceDir), java:fromString(targetZipFile));
}

public function unzip(string sourceZipFile, string targetDir) returns error? {
    unzipExt(java:fromString(sourceZipFile), java:fromString(targetDir));
}