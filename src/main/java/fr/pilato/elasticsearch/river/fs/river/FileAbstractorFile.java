/*
 * Licensed to David Pilato (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.pilato.elasticsearch.river.fs.river;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class FileAbstractorFile extends FileAbstractor<File> {
    public FileAbstractorFile(FsRiverFeedDefinition fsDef) {
        super(fsDef);
    }

    @Override
    public FileAbstractModel toFileAbstractModel(String path, File file) {
        FileAbstractModel model = new FileAbstractModel();
        model.name = file.getName();
        model.file = file.isFile();
        model.directory = !model.file;
        model.lastModifiedDate = file.lastModified();
        model.path = path;
        model.fullpath = file.getAbsolutePath();
        return model;
    }

    @Override
    public InputStream getInputStream(FileAbstractModel file) throws Exception {
        return new FileInputStream(new File(file.fullpath));
    }

    @Override
    public Collection<FileAbstractModel> getFiles(String dir) {
        if (logger.isDebugEnabled()) logger.debug("Listing local files from {}", dir);
        File[] files = new File(dir).listFiles();

        Collection<FileAbstractModel> result = new ArrayList<FileAbstractModel>(files.length);

        // Iterate other files
        for (File file : files) {
            result.add(toFileAbstractModel(dir, file));
        }

        if (logger.isDebugEnabled()) logger.debug("{} local files found", result.size());
        return result;
    }


}
