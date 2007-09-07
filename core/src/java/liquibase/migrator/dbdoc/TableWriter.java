package liquibase.migrator.dbdoc;

import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.migrator.change.Change;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableWriter extends HTMLWriter {

    public TableWriter(File rootOutputDir) {
        super(new File(rootOutputDir, "tables"));
    }

    protected String createTitle(Object object) {
        return object.toString() + " (Table)";
    }

    protected void writeCustomHTML(FileWriter fileWriter, Object object, List<Change> changes) throws IOException {
        writeColumns(fileWriter, ((Table) object));
    }

    private void writeChanges(FileWriter fileWriter, Table table, List<Change> changes) throws IOException {
        List<List<String>> cells = new ArrayList<List<String>>();

        if (changes == null) {
            cells.add(Arrays.asList("None Found"));
        } else {
            for (Change change : changes) {
                cells.add(Arrays.asList(change.getChangeSet().getDatabaseChangeLog().getLogicalFilePath(),
                        change.getChangeSet().getId(),
                        change.getChangeSet().getAuthor(),
                        change.getConfirmationMessage()));
            }
        }

        writeTable("Changes Affecting \""+table+"\"", cells, fileWriter);

    }

    private void writeColumns(FileWriter fileWriter, Table table) throws IOException {
        List<List<String>> cells = new ArrayList<List<String>>();

        for (Column column : table.getColumns()) {
            cells.add(Arrays.asList(column.getTypeName(),
                    "<A HREF=\"../columns/" + table.getName() + "." + column.getName() + ".html" + "\">" + column.getName() + "</A>"));
            //todo: add foreign key info to columns?
        }


        writeTable("Current Columns", cells, fileWriter);

    }
}
