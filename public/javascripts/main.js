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
    listElement.innerText = userName
    var addBtn = document.createElement("button")
    addBtn.innerText = "ADD"
    addBtn.addEventListener("click",  (event)=>{
      console.log(userName)
    })
    listDiv.appendChild(listElement)
    listDiv.appendChild(addBtn)
    return listDiv
  })
  listOfLinkedUsers.forEach((node)=>{
    linkedUsers.appendChild(node)
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

