PAPER_URL="https://yivesmirror.com/files/paper/Paper-1.16.3-latest.jar"
DEPENDENCY_PATH="$PWD/paper.jar"

GROUP_ID="com.destroytokyo.paper"
ARTIFACT_ID="paper-server"
VERSION="1.16.3"
PACKAGING="jar"

echo "Downloading paper"
curl -s "$PAPER_URL" -o "$DEPENDENCY_PATH"
echo "Installing paper"
mvn install:install-file -Dfile="$DEPENDENCY_PATH" -DgroupId="$GROUP_ID" -DartifactId="$ARTIFACT_ID" -Dversion="$VERSION" -Dpackaging="$PACKAGING"
echo "Installed paper"
rm $DEPENDENCY_PATH
echo "Removing local paper file"

GROUP_ID="com.kirelcodes.miniaturepets"
ARTIFACT_ID="miniaturepets"
VERSION="2.5.3"
DEPENDENCY_PATH="$PWD/../dependecies/MiniaturePets.jar"

echo "Installing miniaturepets"
mvn install:install-file -Dfile="$DEPENDENCY_PATH" -DgroupId="$GROUP_ID" -DartifactId="$ARTIFACT_ID" -Dversion="$VERSION" -Dpackaging="$PACKAGING"
echo "Installed miniaturepets"


