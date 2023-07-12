package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileTreeImpl implements FileTree {

    @Override
    public Optional<String> tree(Path path) {
        File file = new File(String.valueOf(path));
        if (!file.exists()) {
            return Optional.empty();
        }
        if (file.isFile()) {
            return Optional.of(file.getName() + " " + file.length() + " bytes");
        }
        if (file.isDirectory()) {
            return Optional.of(buildDirectoryTree(file, new ArrayList<>()));
        }
        return Optional.empty();
    }

    private String buildDirectoryTree(File folder, List<Boolean> lastFolders) {
        StringBuilder directory = new StringBuilder();
        if (lastFolders.size() != 0) {
            directory.append(lastFolders.get(lastFolders.size() - 1) ? "└─ " : "├─ ");
            //├─ └─
        }
        directory.append(folder.getName()).append(" ").append(folderSize(folder));

        File[] files = folder.listFiles();
        int count = files.length;
        files = sortFiles(files);

        for (int i = 0; i < count; i++) {
            directory.append("\n");
            for (Boolean lastFolder : lastFolders) {
                if (lastFolder) {
                    directory.append("   ");
                } else {
                    directory.append("│  ");
                }
            }

            if (files[i].isFile()) {
                directory.append(i + 1 == count ? "└" : "├")
                        .append("─ ")
                        .append(files[i].getName())
                        .append(" ")
                        .append(files[i].length())
                        .append(" bytes");
            }

            if (files[i].isDirectory()) {
                    ArrayList<Boolean> list = new ArrayList<>(lastFolders);
                    list.add(i + 1 == count);
                    directory.append(buildDirectoryTree(files[i], list));
            }

        }
        return directory.toString();
    }

    private long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }else {
                size += getFolderSize(file);
            }
        }
        return size;
    }
    private String folderSize(File folder) {
        return getFolderSize(folder) + " bytes";
    }

    private File[] sortFiles(File[] folder) {
        Arrays.sort(folder);
        List<File> sorted = new ArrayList<>();

        for (File directory : folder) {
            if (directory.isDirectory()) {
                sorted.add(directory);
            }
        }
        for (File file : folder) {
            if (file.isFile()) {
                sorted.add(file);
            }
        }

        return sorted.toArray(new File[0]);
    }
}
