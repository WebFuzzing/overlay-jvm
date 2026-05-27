package com.webfuzzing.overlayjvm;

import org.noear.snack4.ONode;
import org.noear.snack4.jsonpath.JsonPath;


public class JsonPathUtils {

    /**
     * Given an input jsonpath, find the closest match in the current tree.
     * If no target node exists for the jsonpath, this function looks at the jsonpath
     * without its last token, making the jsonpath shorter.
     * For example, if '$.a.b.c' matches nothing, the next path that is checked is '$.a.b'.
     * This process is repeated recursively until a reduced jsonpath is found for which at
     * least one node is found.
     * <p>
     * Note that 'n.exists(p) == true' implies 'closestMatch(n,p) == p'.
     */
    public static String closestMatch(ONode root, String jsonpath) {
        if(root == null || jsonpath == null){
            throw new IllegalArgumentException("Null inputs");
        }
        if (jsonpath.isEmpty()) {
            throw new IllegalArgumentException("jsonpath is empty");
        }
        if(!jsonpath.startsWith("$")) {
            throw new IllegalArgumentException("jsonpath must start with '$'");
        }

        JsonPath path = JsonPath.parse(jsonpath);
        if (!path.select(root).isEmpty()) {
            return jsonpath;
        }

        while (path.getSegmentCount() > 1) {
            path = path.subPath(path.getSegmentCount() - 1);
            if (!path.select(root).isEmpty()) {
                return path.getExpression();
            }
        }

        return "$";
    }
}
