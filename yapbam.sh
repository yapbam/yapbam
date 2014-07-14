HERE="$(dirname "$(readlink -f "$0")")"
cd "${HERE}"
java -jar App/yapbam.jar
