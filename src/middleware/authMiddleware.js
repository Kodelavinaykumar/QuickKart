import jwt from 'jsonwebtoken';

/**
 * Auth middleware with optional refresh token handling
 * If access token expired and valid refresh token is provided, attaches new access token
 */
export const authMiddleware = (req, res, next) => {
  try {
    const authHeader = req.headers['authorization'];
    const refreshToken = req.headers['x-refresh-token']; // optional header for refresh token

    if (!authHeader) {
      return res.status(401).json({ error: 'Authorization header missing' });
    }

    const parts = authHeader.split(' ');
    if (parts.length !== 2 || parts[0] !== 'Bearer') {
      return res
        .status(401)
        .json({ error: 'Authorization header must be in the format: Bearer <token>' });
    }

    const token = parts[1];

    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
      if (err) {
        // Access token expired
        if (err.name === 'TokenExpiredError') {
          if (!refreshToken) {
            return res.status(403).json({ error: 'Token expired, please use refresh token' });
          }

          // Try to verify refresh token
          jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET, (refreshErr, refreshDecoded) => {
            if (refreshErr) {
              return res.status(403).json({ error: 'Invalid/Expired refresh token' });
            }

            // Issue new access token
            const newAccessToken = jwt.sign(
              { id: refreshDecoded.id, email: refreshDecoded.email },
              process.env.JWT_SECRET,
              { expiresIn: '15m' }
            );

            // Attach user info and new access token
            req.user = refreshDecoded;
            res.setHeader('x-access-token', newAccessToken);
            next();
          });
        } else {
          return res.status(403).json({ error: 'Invalid token' });
        }
      } else {
        req.user = decoded;
        next();
      }
    });
  } catch (err) {
    console.error('Auth middleware error:', err);
    res.status(500).json({ error: 'Authentication failed' });
  }
};