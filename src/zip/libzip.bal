import ballerinax/java;
import ballerinax/java.arrays;

function listEntriesExt(handle path) returns handle = @java:Method {
    name: "listEntries",
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