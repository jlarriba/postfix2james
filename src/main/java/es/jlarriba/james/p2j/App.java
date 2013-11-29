/**
 * Copyright 2013 Juan Larriba

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package es.jlarriba.james.p2j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java -jar postfix2james.jar <origin_maildir> <new_maildir>");
        } else {
            Files.walkFileTree(Paths.get(args[0]), new IterateFiles(Paths.get(args[1])));
        }
    }
    
}
