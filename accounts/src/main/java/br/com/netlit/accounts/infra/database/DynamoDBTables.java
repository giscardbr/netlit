package br.com.netlit.accounts.infra.database;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.amazonaws.services.dynamodbv2.util.TableUtils.createTableIfNotExists;

@Log4j2
@Component
public class DynamoDBTables {

    private static final long READ_CAPACITY_UNITS = 1L;
    private static final long WRITE_CAPACITY_UNITS = 1L;

    private final String tablePrefix;
    private final AmazonDynamoDB dynamoDB;

    public DynamoDBTables(@Value("${aws.dynamodb.table-prefix}") final String tablePrefix, final AmazonDynamoDB dynamoDB) {
        this.tablePrefix = tablePrefix;
        this.dynamoDB = dynamoDB;
    }

    public void createTableForEntity(final Class<?> entityClass) {
        log.debug("Creating table of entity {}", entityClass.getSimpleName());
        final ProvisionedThroughput throughput = new ProvisionedThroughput(READ_CAPACITY_UNITS, WRITE_CAPACITY_UNITS);
        final DynamoDBMapperConfig config = TableNameOverride.withTableNamePrefix(this.tablePrefix).config();
        final CreateTableRequest createRequest = new DynamoDBMapper(this.dynamoDB, config)
                .generateCreateTableRequest(entityClass)
                .withProvisionedThroughput(throughput);
        Optional.of(createRequest)
                .map(CreateTableRequest::getGlobalSecondaryIndexes)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(secondaryIndex -> secondaryIndex.setProvisionedThroughput(throughput));
        createTableIfNotExists(this.dynamoDB, createRequest);
    }

    public void deleteTableForEntity(final Class<?> entityClass) {
        log.debug("Deleting table of entity {}", entityClass.getSimpleName());
        final DynamoDBMapperConfig config = TableNameOverride.withTableNamePrefix(this.tablePrefix).config();
        final DeleteTableRequest deleteRequest = new DynamoDBMapper(this.dynamoDB, config)
                .generateDeleteTableRequest(entityClass);
        TableUtils.deleteTableIfExists(this.dynamoDB, deleteRequest);
    }
}
