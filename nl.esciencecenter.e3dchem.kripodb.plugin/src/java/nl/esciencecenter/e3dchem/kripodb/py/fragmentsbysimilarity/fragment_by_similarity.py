from kripodb.canned import similarities

# Map options to kripo arguments
query_column = options['fragment_id_column']
similarity_matrix = options['matrix']
cutoff = options['cutoff']
limit = options['limit']

# Select input column
queries = input_table[query_column]

# Fetch similarities
output_table = similarities(queries,
                            similarity_matrix,
                            cutoff,
                            limit)