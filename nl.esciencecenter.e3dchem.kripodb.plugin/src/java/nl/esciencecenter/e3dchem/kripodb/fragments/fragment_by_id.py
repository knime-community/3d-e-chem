import pandas as pd
from kripodb.canned import fragments_by_pdb_codes, fragments_by_id

# Map options to kripo arguments
id_column = options['id_column']
fragments_db_filename = options['fragmentsdb']
id_type = options['id_type']

# Select input column
queries = input_table[id_column]

# Fetch fragments
if id_type == 'pdb':
    output_table = fragments_by_pdb_codes(queries, fragments_db_filename)
elif id_type == 'fragment':
    output_table = fragments_by_id(queries, fragments_db_filename)
else:
    raise NotImplementedError('Type of identifier unknown')
