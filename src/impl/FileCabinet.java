package impl;

import core.Cabinet;
import core.Folder;
import core.MultiFolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet() {
        this.folders = new ArrayList<>();
    }

    public void addFolder(Folder folder) {
        folders.add(folder);
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findFolderByNameRecursive(folders, name);
    }

    private Optional<Folder> findFolderByNameRecursive(List<Folder> folderList, String name) {
        for (Folder folder : folderList) {
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }
            if (folder instanceof MultiFolder) {
                Optional<Folder> result = findFolderByNameRecursive(((MultiFolder) folder).getFolders(), name);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        return findFoldersBySizeRecursive(folders, size);
    }

    private List<Folder> findFoldersBySizeRecursive(List<Folder> folderList, String size) {
        List<Folder> result = new ArrayList<>();
        for (Folder folder : folderList) {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }
            if (folder instanceof MultiFolder) {
                result.addAll(findFoldersBySizeRecursive(((MultiFolder) folder).getFolders(), size));
            }
        }
        return result;
    }

    @Override
    public int count() {
        return countFolders(folders);
    }

    private int countFolders(List<Folder> folderList) {
        int count = folderList.size();
        for (Folder folder : folderList) {
            if (folder instanceof MultiFolder) {
                count += countFolders(((MultiFolder) folder).getFolders());
            }
        }
        return count;
    }
}