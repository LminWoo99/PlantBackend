import React, { useState, useEffect } from "react";
import axios from "axios";
import './App.css';

const App = () => {
  const [items, setItems] = useState([]);
  const [tTitle, setTTitle] = useState("");
  const [tContent, setTContent] = useState("");

  useEffect(() => {
    axios.get("/post")
      .then(response => setItems(response.data))
      .catch(error => console.log(error));
  }, []);

  const [textareaHeight, setTextareaHeight] = useState({
    row: 1,
    lineBreak: {},
  });

  const handleTTitleChange = (event) => {
    setTTitle(event.target.value);
  };

  const handleTContentChange = (event) => {
    setTContent(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!tTitle || !tContent) {
      return;
    }

    const newItem = {
      title: tTitle,
      content: tContent
    };
    axios.post("/post", newItem)
      .then(response => setItems([...items, response.data.tradeBoardDto]))
      .catch(error => console.log(error));
    setTTitle("");
    setTContent("");
  };

  const handleDelete = (id) => {
    axios.delete(`/post/${id}`)
      .then(response => setItems(items.filter(item => item.id !== id)))
      .catch(error => console.log(error));
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <h1>식구2</h1>
          <select>
            <option>게시판 종류</option>
          </select>
        </div>
        <div>
          <h3>제목</h3> <input type='text' value={tTitle} onChange={handleTTitleChange}/>
        </div>
        <div>
          <h3>내용</h3><textarea value={tContent} onChange={handleTContentChange}></textarea>
        </div>
        <button type="submit">등록</button>
      </form>

    </div>
  );
};

export default App;
