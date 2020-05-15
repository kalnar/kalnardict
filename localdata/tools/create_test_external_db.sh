here=$(dirname "${BASH_SOURCE[0]}")

DATABASE_ASSETS_FOLDER="${here}"/../src/main/assets/database/
UNIT_TEST_RESOURCE_FOLDER="${here}"/../src/test/resources/

mkdir -p $DATABASE_ASSETS_FOLDER
mkdir -p $UNIT_TEST_RESOURCE_FOLDER
db_path=${DATABASE_ASSETS_FOLDER}/test_external.db

rm -f "${db_path}"
sqlite3 "${db_path}" <"${here}"/test_external_db.sql
