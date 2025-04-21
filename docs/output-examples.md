# Output examples

## Book Purchase Message
### `Post bazar-frontend-url:8080/purchase/{id};`

**input:** (url) id = 1

**output:**  Purchased: RPCs for Noobs.

## Query Response

### Search by topics `(GET bazar-frontend-url:8080/search/{topic})`

**input:** (url) topic = "distributed%20systems"

**output:**
```
[
    {
        "id": 1,
        "title": "RPCs for Noobs."
    },
    {
        "id": 2,
        "title": "How to get a good grade in DOS in 40 minutes a day."
    }
]
```

### Book Info `(GET bazar-frontend-url:8080/info/{id})`

**input:** (url) id = 3 

**output:**
```
{
    "title": "Xen and the Art of Surviving Undergraduate School.",
    "quantity": 120,
    "price": 29.99
}
```
## Error Response

### Query Error Response `(GET bazar-frontend-url:8080/info/{id})`

**input:** (url) id = 5

**output:**  No item found with id: 5

### Purchase Error Response  `(Post bazar-frontend-url:8080/purchase/{id})`
**input:** (url) id = 5

**output:**  Book with id: " + id + " is not found
