.phoney: clean schemablock schemapass queryblock querypass deploy all pushschemablock pushschemapass pushqueryblock pushquerypass

clean:
	mvn clean

feature:
	mvn -pl functional-suite -am test

all: pushschemablock pushschemapass pushqueryblock pushquerypass
	kubectl delete -k k8s
	kubectl apply -k k8s

pushschemablock: schemablock
	docker push tsmarsh/schemablock

pushschemapass: schemapass
	docker push tsmarsh/schemapass

pushqueryblock: queryblock
	docker push tsmarsh/queryblock

pushquerypass: querypass
	docker push tsmarsh/querypass

schemablock: schemablock/target/SHADE.jar
	docker build -t tsmarsh/schemablock:latest schemablock

schemapass: schemapass/target/SHADE.jar
	docker build -t tsmarsh/schemapass:latest schemapass

queryblock: queryblock/target/SHADE.jar
	docker build -t tsmarsh/queryblock:latest queryblock

querypass: querypass/target/SHADE.jar
	docker build -t tsmarsh/querypass:latest querypass

schemablock/target/SHADE.jar:
	mvn -pl schemablock -am clean package

schemapass/target/SHADE.jar:
	mvn -pl schemapass -am clean package

queryblock/target/SHADE.jar:
	mvn -pl queryblock -am clean package

querypass/target/SHADE.jar:
	mvn -pl querypass -am clean package