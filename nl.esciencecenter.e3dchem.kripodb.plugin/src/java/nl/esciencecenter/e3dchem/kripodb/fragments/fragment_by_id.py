import pandas as pd
from kripodb.canned import fragments_by_pdb_codes, fragments_by_id

# Map options to kripo arguments
id_column = options['id_column']
fragments_db_filename = options['fragmentdb']
id_type = options['id_type']
prefix = options['prefix']

# Select input column
queries = input_table[id_column]

# Fetch fragments
if id_type == 'pdb':
    fragments = fragments_by_pdb_codes(queries, fragments_db_filename, prefix)
elif id_type == 'fragment':
    fragments = fragments_by_id(queries, fragments_db_filename, prefix)
else:
    raise NotImplementedError('Type of identifier unknown')

output_table = pd.merge(input_table, 
                        fragments, 
                        left_on=id_column,
                        right_on=prefix + u'frag_id',
                        how='left')