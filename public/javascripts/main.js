var getUser = document.querySelector(".getUser")
var searchBtn = document.querySelector(".searchBtn")
var linkedUsers = document.querySelector(".linkedUsers")
searchBtn.addEventListener("click", async (event)=>{
  var foundUsers = await searchUsers(getUser.value)
  console.log(foundUsers)
  linkedUsers.innerHTML = ""
  var listOfLinkedUsers = foundUsers.map((userName)=>{
    var listDiv = document.createElement("div")
    var listElement = document.createElement("li")
    var br = document.createElement("br")
    listElement.innerText = userName
    var addBtn = document.createElement("button")
    addBtn.innerHTML = "<i class=\"fa fa-plus-square-o\">"
    addBtn.addEventListener("click",  (event)=>{
      console.log(userName)
    })
    listDiv.appendChild(listElement)
    if(userName !== "Input some data to search for") {
      listDiv.appendChild(br)
      listDiv.appendChild(addBtn)
    }
    return listDiv
  })
  listOfLinkedUsers.forEach((node)=>{
    linkedUsers.appendChild(node).style.position = 'relative'
  })
})

async function searchUsers(searchString) {
  var result = await fetch(
    "/search",
    {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({"searchString": searchString})
    }
  )
  return result.json()
}

