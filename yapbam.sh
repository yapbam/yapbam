HERE="$(dirname "$(test -L "$0" && readlink "$0" || echo "$0")")"
cd "${HERE}"
java -jar App/yapbam.jar
