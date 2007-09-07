package liquibase.migrator.change;

import liquibase.database.Database;
import liquibase.database.MySQLDatabase;
import liquibase.database.structure.DatabaseObject;
import liquibase.database.structure.Table;
import liquibase.migrator.exception.UnsupportedChangeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.Set;

/**
 * Removes an existing unique constraint.
 */
public class DropUniqueConstraintChange extends AbstractChange {
    private String tableName;
    private String constraintName;

    public DropUniqueConstraintChange() {
        super("dropUniqueConstraint", "Drop Unique Constraint");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public String[] generateStatements(Database database) throws UnsupportedChangeException {
        if (database instanceof MySQLDatabase) {
            return new String[]{ "ALTER TABLE " + getTableName() + " DROP KEY " + getConstraintName(), };
        }

        return new String[]{
                "ALTER TABLE " + getTableName() + " DROP CONSTRAINT " + getConstraintName(),
        };
    }

    public String getConfirmationMessage() {
        return "Unique Constraint Key Dropped";
    }

    public Element createNode(Document currentChangeLogFileDOM) {
        Element node = currentChangeLogFileDOM.createElement(getTagName());
        node.setAttribute("tableName", getTableName());
        node.setAttribute("constraintName", constraintName);
        return node;
    }

    public Set<DatabaseObject> getAffectedDatabaseObjects() {

        Set<DatabaseObject> returnSet = new HashSet<DatabaseObject>();

        Table table = new Table();
        table.setName(tableName);
        returnSet.add(table);

        return returnSet;

    }
}
