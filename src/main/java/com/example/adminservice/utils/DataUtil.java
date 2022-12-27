package com.example.adminservice.utils;

import com.example.adminservice.dto.TreeNodeDTO;
import com.example.adminservice.model.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataUtil {
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }

    public static List<TreeNodeDTO> buildTree(List<Function> functions) {
        List<TreeNodeDTO> list = new ArrayList<>();
        List<Function> parentList = getChildren(functions, null);
        if (parentList.isEmpty()) {
            TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
            treeNodeDTO.setData(functions);
            list.add(treeNodeDTO);
        } else {
            for (Function f : parentList) {
                TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
                treeNodeDTO.setData(f);
                treeNodeDTO.setTitle(f.getName());
                treeNodeDTO.setKey(f.getCode());
                treeNodeDTO.setValue(f.getCode());
                functions.remove(f);
                buildTree(treeNodeDTO, f.getCode(), functions);
                list.add(treeNodeDTO);
            }
        }
        return list;
    }

    public static void buildTree(TreeNodeDTO node, String parentCode, List<Function> functions) {
        if (functions.size() > 0) {
            List<Function> listChild = getChildren(functions, parentCode);
            if (listChild.size() > 0) {
                for (Function f : listChild) {
                    TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
                    treeNodeDTO.setData(f);
                    treeNodeDTO.setTitle(f.getName());
                    treeNodeDTO.setKey(f.getCode());
                    treeNodeDTO.setValue(f.getCode());
                    functions.remove(f);
                    buildTree(treeNodeDTO, f.getCode(), functions);
                    node.addChildren(treeNodeDTO);
                }
            }
        }
    }

    public static List<Function> getChildren(List<Function> functions, String parentCode) {
        return functions.stream()
                .filter(f -> Objects.equals(f.getParentCode(), parentCode))
                .collect(Collectors.toList());
    }

    public static List<TreeNodeDTO> buildTreeSelection(List<Function> functions) {
        List<TreeNodeDTO> list = new ArrayList<>();
        List<Function> parentList = getChildren(functions, null);
        if (parentList.isEmpty()) {
            TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
            treeNodeDTO.setData(functions);
            functions.stream().forEach(function -> {
                treeNodeDTO.setKey(function.getCode());
                treeNodeDTO.setTitle(function.getCode());
                treeNodeDTO.setValue(function.getName());
            });
            list.add(treeNodeDTO);
        } else {
            for (Function f : parentList) {
                TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
                treeNodeDTO.setData(f);
                treeNodeDTO.setKey(f.getCode());
                treeNodeDTO.setTitle(f.getCode());
                treeNodeDTO.setValue(f.getName());
                functions.remove(f);
                buildTreeSelection(treeNodeDTO, f.getCode(), functions);
                list.add(treeNodeDTO);
            }
        }
        return list;
    }

    public static void buildTreeSelection(TreeNodeDTO node, String parentCode, List<Function> functions) {
        if (functions.size() > 0) {
            List<Function> listChild = getChildren(functions, parentCode);
            if (listChild.size() > 0) {
                for (Function f : listChild) {
                    TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
                    treeNodeDTO.setData(f);
                    treeNodeDTO.setKey(f.getCode());
                    treeNodeDTO.setTitle(f.getCode());
                    treeNodeDTO.setValue(f.getName());
                    functions.remove(f);
                    buildTreeSelection(treeNodeDTO, f.getCode(), functions);
                    node.addChildren(treeNodeDTO);
                }
            }
        }
    }
}
