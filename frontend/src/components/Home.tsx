import { MouseEventHandler } from "react";
import { useNavigate } from "react-router-dom"

function Home() {
    const navigate = useNavigate();

    const createGame = async () => {
        const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/games`, {
            method: 'POST'
        });

        const data = await response.json();

        // store local information
        localStorage.setItem(`playerId_${data.gameId}`, data.playerId);
        localStorage.setItem(`color_${data.gameId}`, data.color);

        navigate(`/game/${data.gameId}`);
    }


    return (
        <>
            <div className="home">
                <h2>Welcome to Blockus</h2>
                <button onClick={createGame}>Create Game</button>
            </div>
        </>
    )
}

export default Home