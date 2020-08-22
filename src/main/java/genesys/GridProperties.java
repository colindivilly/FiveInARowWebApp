package genesys;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grid")
public class GridProperties {
    private int columns;
    private int rows;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
