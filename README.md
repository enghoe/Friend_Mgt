# Friend_Mgt
A RESTful API application for friends management

## Build and Run

Execute the following commands under your preferred directory.

```
git clone https://github.com/enghoe/Friend_Mgt.git
cd Friend_Mgt/
chmod +x gradlew
./gradlew bootRun
```

## APIs
### Friend Connection
#### Create new friend connection
* URL
>   **POST** /friends/add
* Sample request
```
{
  "friends":
    [
      "andy@example.com",
      "john@example.com"
    ]
}
 ```
* Sample response
```
{
  "success": "true"
}
```
#### Retrieve the friends list for an email address
* URL
>   **POST** /friends/search
* Sample request
```
{
  "email": "andy@example.com"
}
 ```
* Sample response
```
{
  "success": "true",
  "friends":
    [
      "john@example.com"
    ],
  "count": 1
}
```
#### Retrieve common friends list between two email addresses
* URL
>   **POST** /friends/common
* Sample request
```
{
  "friends":
    [
      "andy@example.com",
      "john@example.com"
    ]
}
 ```
* Sample response
```
{
  "success": "true",
  "friends":
    [
      "common@example.com"
    ],
  "count": 1
}
```

### Block updates from an email address
* URL
>   **POST** /block
* Sample request
```
{
  "requestor": "andy@example.com",
  "target": "john@example.com"
}
 ```
* Sample response
```
{
  "success": "true"
}
```
### Messages
#### Post a message
* URL
>   **POST** /messages/post
* Sample request
```
{
  "sender":  "john@example.com",
  "text": "Hello World! kate@example.com"
}
 ```
* Sample response
```
{
  "success": "true"
  "recipients":
    [
      "lisa@example.com",
      "kate@example.com"
    ]
}
```
#### Subscribe for updates
* URL
>   **POST** /messages/subscribe
* Sample request
```
{
  "requestor": "andy@example.com",
  "target": "john@example.com"
}
 ```
* Sample response
```
{
  "success": "true"
}
```
