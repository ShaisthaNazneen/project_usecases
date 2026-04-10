print("Social Media Content Sanitizer")
def Post_TextCleaned(content,Banned_Words):
    for word in Banned_Words:
        content=content.replace(word,"***")
        content=content.replace(word.capitalize(),"***")
        content=content.replace(word.upper(),"***")
    return content
def extract_links(content):
    words=content.split()
    temp_links=[]
    for j in words:
        if j.startswith("http"):
            temp_links.append(j)
    return temp_links
#List of Posts
posts = [
    ("Shaistha", "small steps lead to big results"),
    ("Mahira", "I hate when app crashes http://helpdesk.in"),
    ("Rahul", "Such a toxic argument happening here"),
    ("Shaistha", "Check this http://brainly.in for concepts"),
    ("Mahira", "Bad network had ruined my test"),
    ("Ayra", "The  session was very helpful today!"),
    ("Rahul", "Stop spreading hate in comments"),
    ("Shaistha", "This platform is becoming toxic day by day"),
    ("bety","bad bad toxic hate")
]
#Banned Words
Banned_Words = ["toxic","hate", "bad", "stupid", "ugly"]
Total_p=0
Cleaned_p=0
Blocked_p=0
links = []
users = {}
for creator,i in posts:
    Total_p+=1
    if creator not in users:
        users[creator] = 0

    # No of issues
    lower_text=i.lower()
    issue_count=0
    for word in Banned_Words:
        issue_count += lower_text.count(word)
    users[creator]+=issue_count
    # cleaning
    clean_p = Post_TextCleaned(i, Banned_Words)
    if issue_count==0:
        status="SAFE"
        final_p=clean_p
    elif issue_count==1:
        status = "CLEANED"
        Cleaned_p += 1
        final_p=clean_p
    else:
        status="BLOCKED"
        Blocked_p+=1
        final_p="POST REMOVED"
    # extract links
    Links_Extracted=extract_links(i)
    links.extend(Links_Extracted)
    print("\nUser:", creator)
    print("Post:", final_p)
    print("Status:",status)
#saving links to file
with open("links_found.txt", "w") as file:
    for k in links:
        file.write(k + "\n")
#user issue report
print("\nUser Issue Count:")
for u in users:
    print(u,":",users[u])
print("\n----- Final Report -----")
print("Total Posts Screened:",Total_p)
print("Cleaned Posts:",Cleaned_p)
print("Blocked Posts:",Blocked_p)