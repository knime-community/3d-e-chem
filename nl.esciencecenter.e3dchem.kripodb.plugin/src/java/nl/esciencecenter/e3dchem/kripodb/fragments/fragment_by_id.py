from kripodb.canned import fragments_by_pdb_codes, fragments_by_id
from kripodb.version import __version_info__

# Map options to kripo arguments
id_column = options['id_column']
fragments_db_filename = options['fragmentsdb']
id_type = options['id_type']

# Select input column
queries = input_table[id_column]

# Fetch fragments
if __version_info__[0] >= '2' and __version_info__[1] >= '2':
    from kripodb.webservice.client import IncompleteFragments
    import logging

    try:
        if id_type == 'pdb':
            output_table = fragments_by_pdb_codes(queries, fragments_db_filename)
        elif id_type == 'fragment':
            output_table = fragments_by_id(queries, fragments_db_filename)
        else:
            raise NotImplementedError('Type of identifier unknown')
    except IncompleteFragments as e:
        flow_variables['warning_message'] = str(message)
        logging.warning("Following identifier(s) could not be found: " + ",".join(e.absent_identifiers))
        output_table = e.fragments
else:
    if id_type == 'pdb':
        output_table = fragments_by_pdb_codes(queries, fragments_db_filename)
    elif id_type == 'fragment':
        output_table = fragments_by_id(queries, fragments_db_filename)
    else:
        raise NotImplementedError('Type of identifier unknown')
