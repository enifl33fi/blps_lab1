package com.enifl33fi.lab1.api.config.ytsaurus;

import tech.ytsaurus.client.ApiServiceTransaction;
import tech.ytsaurus.client.YTsaurusClient;
import tech.ytsaurus.client.request.*;
import tech.ytsaurus.client.rows.UnversionedRow;
import tech.ytsaurus.client.rows.UnversionedRowset;
import tech.ytsaurus.core.cypress.CypressNodeType;
import tech.ytsaurus.core.cypress.YPath;
import tech.ytsaurus.core.tables.ColumnSchema;
import tech.ytsaurus.core.tables.ColumnSortOrder;
import tech.ytsaurus.core.tables.TableSchema;
import tech.ytsaurus.typeinfo.TiType;
import tech.ytsaurus.ysontree.YTree;

import java.util.List;
import java.util.Map;

public class YtsaurusConnection {
    private final String tablePath = "//home/statistics";
    private final String cluster = "localhost:8000";

    private final YTsaurusClient client;

    private final TableSchema tableSchema = TableSchema.builder()
            .setUniqueKeys(true)
            .add(
                    ColumnSchema.builder("time", TiType.timestamp())
                            .setSortOrder(ColumnSortOrder.ASCENDING)
                            .build()
            )
            .add(
                    ColumnSchema.builder("subName", TiType.string())
                            .build()
            )
            .add(
                    ColumnSchema.builder("clientName", TiType.string())
                            .build()
            )
            .build();

    public YtsaurusConnection(String endpoint, String token) {
        this.client = YTsaurusClient.builder()
                .setCluster(this.cluster)
                .build();

        CreateNode node = CreateNode.builder()
                .setPath(YPath.simple(this.tablePath))
                .setType(CypressNodeType.TABLE)
                .setAttributes(Map.of(
                        "dynamic", YTree.booleanNode(true),
                        "schema", this.tableSchema.toYTree()
                ))
                .setIgnoreExisting(true)
                .build();

        this.client.createNode(node).join();
        this.client.mountTable(this.tablePath).join();
    }

    public void addStatisticRow(long time, String subName, String clientName) {
        System.out.println(subName);
        try (this.client) {
            try (ApiServiceTransaction transaction =
                         client.startTransaction(new StartTransaction(TransactionType.Tablet)).join()) {
                transaction.modifyRows(
                        ModifyRowsRequest.builder()
                                .setPath(this.tablePath)
                                .setSchema(this.tableSchema)
                                .addInsert(List.of(time, subName, clientName))
                                .build()).join();
                transaction.commit().join();
            }

            try (ApiServiceTransaction transaction =
                         this.client.startTransaction(new StartTransaction(TransactionType.Tablet)).join()) {
                UnversionedRowset rowset = transaction.lookupRows(
                        LookupRowsRequest.builder()
                                .setPath(this.tablePath)
                                .setSchema(this.tableSchema.toLookup())
                                .build()).join();

                for (UnversionedRow row : rowset.getRows()) {
                    System.out.println(row.toYTreeMap(tableSchema, true));
                }

                transaction.commit().join();
            }
        }
    }

    public void close() {
        this.client.close();
    }

}
