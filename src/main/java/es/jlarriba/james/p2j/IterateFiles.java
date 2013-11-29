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
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterateFiles extends SimpleFileVisitor<Path> {

    private final Path targetPath;
    private Path sourcePath = null;

    public IterateFiles(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        if (sourcePath == null) {
            sourcePath = dir;
        } else {
            Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        try {
            System.out.println("Parsing file " + file.toString());
            RandomAccessFile inFile = new RandomAccessFile(file.toString(), "r");
            byte[] inBytes = new byte[(int) inFile.length()];
            Byte[] inBytesObj = new Byte[(int) inFile.length()];
            inFile.read(inBytes);

            int i = 0;
            for (byte b : inBytes) {
                inBytesObj[i++] = b;
            }

            List<Byte> list = new ArrayList(Arrays.asList(inBytesObj));

            boolean control = true;

            for (int j = 0; j < list.size(); j++) {
                //System.out.println("ByteValue: " + list.get(j).byteValue());
                if (control) {
                    if (list.get(j).byteValue() == 0x0A) {
                        if (list.get(j + 1).byteValue() == 0x0A) {
                            System.out.println("Postfix pattern found. Converting to James.");
                            list.set(j, new Byte("13"));
                            list.set(j + 1, new Byte("10"));
                            list.add(j + 2, new Byte("13"));
                            list.add(j + 3, new Byte("10"));
                            control = false;
                        }
                    }
                }
            }

            Byte[] outBytesObj = list.toArray(new Byte[list.size()]);
            byte[] outBytes = new byte[outBytesObj.length];

            int k = 0;
            for (Byte b : outBytesObj) {
                outBytes[k++] = b.byteValue();
            }

            RandomAccessFile outFile = new RandomAccessFile(targetPath.resolve(sourcePath.relativize(file)).toString(), "rw");
            outFile.write(outBytes);

            inFile.close();
            outFile.close();
        } catch (Exception exc) {
            System.out.println("File " + file.toString() + " can't be read: " + exc.getLocalizedMessage());
        }
        return CONTINUE;
    }
}
