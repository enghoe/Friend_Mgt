# Friend_Mgt
A RESTful API project for friends management

## APIs
* Friends
  - /friends/add
  
    *Method* POST
    
    *Request*
    `
    {
      friends:
      [
        'andy@example.com',
        'john@example.com'
      ]
    }
    `
    
  - /friends/search
  - /friends/common

* **Block**
  - /block

* **Messages**
  - /messages/subscribe
  - /messages/post
