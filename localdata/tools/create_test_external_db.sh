here=$(dirname "${BASH_SOURCE[0]}")

mkdir -p "${here}"/../src/main/assets/database
db_path=${here}/../src/main/assets/database/test_external.db

rm -f "${db_path}"
sqlite3 "${db_path}" <"${here}"/test_external_db.sql
