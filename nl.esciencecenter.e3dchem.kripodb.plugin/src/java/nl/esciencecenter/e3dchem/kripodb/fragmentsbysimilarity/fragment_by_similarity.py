from kripodb.canned import similarities
from kripodb.version import __version_info__

# Map options to kripo arguments
query_column = options['fragment_id_column']
similarity_matrix = options['matrix']
cutoff = options['cutoff']
limit = options['limit']

# Select input column
queries = input_table[query_column]

# Fetch similarities
if __version_info__[0] >= '2' and __version_info__[1] >= '2':
    from kripodb.canned import IncompleteHits
    import logging
    try:
        output_table = similarities(queries,
                                    similarity_matrix,
                                    cutoff,
                                    limit)
    except IncompleteHits as e:
        output_table = e.hits
        flow_variables['warning_message'] = e.message
        logging.warning("Following fragment identifier(s) could not be found: " + ",".join(e.absent_identifiers))
else:
    output_table = similarities(queries,
                                similarity_matrix,
                                cutoff,
                                limit)
