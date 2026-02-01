package com.carlos.tool.choose.ui;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.table.AbstractTableModel;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class MyTableModel extends AbstractTableModel {

    String[] columnNames;

    // 使用从DAO返回的List作为TableModel的数据

    public final List<List<Object>> data;


    public MyTableModel(List<List<Object>> data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    // heros.size返回一共有多少行
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Object> row = data.get(rowIndex);
        if (columnIndex >= row.size()) {
            return null;
        }
        return row.get(columnIndex);
    }

}
