XML. Practice Task 1
===============
1\. Prepare XSD
---------------
Prepare XSD schema document for the entities, in `model` module.

Root element should have `storage` name.

2\. Update entities
---------------
Update entities in `model` module with JAXB annotations.

3\. Implement XML storage
---------------
Create new implementation of `Storage` interface - `XmlStorage` that 
uses JAXB to store data inside appropriate XML files - XML file name should be set via constructor.

Each entity should have its own Storage implementation - use abstractions where possible.

Ensure that result XML is valid against the XSD schema.

3\. Store
---------------
Update `store` module to use `xml-storage` instead of `jdbc-storage`.

1. Include latest `xml-storage` dependency.
2. Update `Runner` class to create `StoreApp` based on JAXB XML storage.
3. Add examples that demonstrates `update`, `delete`, `find by id` operations.


Note
---------------
This task is connected with: 
1. https://github.com/ChangeRequest/dependency-management.hw1 
2. https://github.com/ChangeRequest/logging.hw1
3. https://github.com/ChangeRequest/jdbc.pr1
