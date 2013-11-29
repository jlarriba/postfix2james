postfix2james
=============

Postfix Maildir messages lack the header and body separation Apache James uses to split messages into header and body while storing them in a SQL database. This utility converts your Postfix maildir messages in James maildir messages, that can be correctly imported into the database using the james-cli.sh copymailbox action.

Usage: java -jar postfix2james.jar <origin_maildir> <new_maildir>
