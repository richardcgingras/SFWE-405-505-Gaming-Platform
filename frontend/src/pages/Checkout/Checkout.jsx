import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './Checkout.css';
import { checkoutWithCreditCard, addGameToCart, getGames, getCartTotal } from "../../services/ShoppingCart.js";

export default function Checkout() {
  const location = useLocation();
  const navigate = useNavigate();

  const [cartItems, setCartItems] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [isInitializing, setIsInitializing] = useState(true);

  const [form, setForm] = useState({
    cardName: '',
    cardNumber: '',
    expiry: '',
    cvv: '',
    email: '',
  });

  const [errors, setErrors]   = useState({});
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [toast, setToast]     = useState('');

  useEffect(() => {
    const initCheckout = async () => {
      try {
        setIsInitializing(true);
        
        // If we came from a specific "Buy Now" button, add that game to cart first
        if (location.state?.game?.id && location.state.game.id !== 'unknown') {
          await addGameToCart(location.state.game.id);
        }
        
        // Fetch current cart contents and total
        const [items, totalData] = await Promise.all([
          getGames(),
          getCartTotal()
        ]);
        
        setCartItems(items || []);
        setTotalPrice(parseFloat(totalData.total) || 0);
      } catch (err) {
        console.error("Failed to initialize checkout:", err);
        showToast("Failed to load cart items.");
      } finally {
        setIsInitializing(false);
      }
    };
    
    initCheckout();
  }, [location.state]);

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(''), 3500);
  };

  const formatCard = (val) =>
    val.replace(/\D/g, '').slice(0, 16).replace(/(.{4})/g, '$1 ').trim();

  const formatExpiry = (val) => {
    const digits = val.replace(/\D/g, '').slice(0, 4);
    return digits.length >= 3 ? `${digits.slice(0, 2)}/${digits.slice(2)}` : digits;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    let formatted = value;
    if (name === 'cardNumber') formatted = formatCard(value);
    if (name === 'expiry')     formatted = formatExpiry(value);
    if (name === 'cvv')        formatted = value.replace(/\D/g, '').slice(0, 4);
    setForm((prev) => ({ ...prev, [name]: formatted }));
    setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const validate = () => {
    const errs = {};
    if (!form.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/))
      errs.email = 'Enter a valid email.';
    if (form.cardName.trim().length < 2)
      errs.cardName = 'Enter the name on your card.';
    if (form.cardNumber.replace(/\s/g, '').length !== 16)
      errs.cardNumber = 'Card number must be 16 digits.';
    if (!form.expiry.match(/^(0[1-9]|1[0-2])\/\d{2}$/))
      errs.expiry = 'Enter expiry as MM/YY.';
    if (form.cvv.length < 3)
      errs.cvv = 'CVV must be 3 digits.';
    return errs;
  };

  const handleSubmit = async () => {
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }
  
    setLoading(true);
    try {
      await checkoutWithCreditCard({
        "cardNumber": form.cardNumber.replace(/\s/g, ''),
        "cardHolderName": form.cardName,
        "expiryDate": form.expiry,
        "cvv": form.cvv
      });
      setSuccess(true);
    } catch (err) {
      showToast(err.message || 'Payment failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="page">
        <div className="bg-glow" />
        <div className="checkout-success-container">
          <div className="success-box">
            <div className="success-icon">✓</div>
            <h2 className="success-title">PURCHASE COMPLETE</h2>
            <p className="success-sub">
              {cartItems.length === 1 
                ? <><span className="success-game-name">{cartItems[0].name}</span> has been added to your library.</>
                : `${cartItems.length} games have been added to your library.`
              }
            </p>
            <p className="success-email">Confirmation sent to <strong>{form.email}</strong></p>
            <div className="success-actions">
              <button className="btn btn-red" onClick={() => navigate('/library')}>
                Go to Library
              </button>
              <button className="btn btn-ghost" onClick={() => navigate('/')}>
                Back to Store
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="bg-glow" />

      {toast && <div className="toast">{toast}</div>}

      {/* NAV */}
      <nav className="nav">
        <div className="nav-logo">
        </div>
        <button className="btn btn-ghost" onClick={() => navigate(-1)}>
          ← Back
        </button>
      </nav>

      <div className="checkout-layout">

        {/* ── LEFT: Order Summary ── */}
        <div className="checkout-summary">
          <p className="checkout-section-label">ORDER SUMMARY</p>

          <div className="order-items-list">
            {isInitializing ? (
              <p style={{ color: 'var(--text-muted)', textAlign: 'center', padding: '20px' }}>Loading items...</p>
            ) : cartItems.length === 0 ? (
              <p style={{ color: 'var(--text-muted)', textAlign: 'center', padding: '20px' }}>No items in cart.</p>
            ) : (
              cartItems.map((item) => (
                <div key={item.id} className="order-game-card">
                  <div className="order-game-art">
                    {item.image
                      ? <img src={item.image} alt={item.name} />
                      : <div className="order-game-art-placeholder">🎮</div>
                    }
                  </div>
                  <div className="order-game-details">
                    <p className="order-game-title">{item.name}</p>
                    {item.category && item.category.length > 0 && (
                      <p className="order-game-genre">{item.category[0].type}</p>
                    )}
                    <p className="order-game-type">Digital Download</p>
                  </div>
                  <div className="order-item-price" style={{ fontWeight: 700, color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                    ${item.price.toFixed(2)}
                  </div>
                </div>
              ))
            )}
          </div>

          <div className="order-breakdown">
            <div className="order-line">
              <span>Subtotal ({cartItems.length} items)</span>
              <span>${totalPrice.toFixed(2)}</span>
            </div>
            <div className="order-line">
              <span>Platform Fee</span>
              <span>$0.00</span>
            </div>
            <div className="order-line order-line-total">
              <span>Total</span>
              <span className="order-total-price">${totalPrice.toFixed(2)}</span>
            </div>
          </div>

          <div className="order-secure-badge">
            <span className="secure-icon">🔒</span>
            <span>Secured &amp; encrypted checkout</span>
          </div>
        </div>

        {/* ── RIGHT: Payment Form ── */}
        <div className="checkout-form-panel">
          <p className="checkout-section-label">PAYMENT DETAILS</p>

          <div className="checkout-form">

            {/* Email */}
            <div className="form-group">
              <label className="form-label">Email Address</label>
              <input
                className={`form-input${errors.email ? ' input-error' : ''}`}
                type="email"
                name="email"
                placeholder="you@example.com"
                value={form.email}
                onChange={handleChange}
              />
              {errors.email && <span className="field-error">{errors.email}</span>}
            </div>

            <div className="form-divider">
              <span>Card Information</span>
            </div>

            {/* Card Name */}
            <div className="form-group">
              <label className="form-label">Name on Card</label>
              <input
                className={`form-input${errors.cardName ? ' input-error' : ''}`}
                type="text"
                name="cardName"
                placeholder="John Doe"
                value={form.cardName}
                onChange={handleChange}
              />
              {errors.cardName && <span className="field-error">{errors.cardName}</span>}
            </div>

            {/* Card Number */}
            <div className="form-group">
              <label className="form-label">Card Number</label>
              <div className="card-input-wrapper">
                <input
                  className={`form-input${errors.cardNumber ? ' input-error' : ''}`}
                  type="text"
                  name="cardNumber"
                  placeholder="0000 0000 0000 0000"
                  value={form.cardNumber}
                  onChange={handleChange}
                  inputMode="numeric"
                />
                <div className="card-icons">
                  <span className="card-icon-visa">VISA</span>
                  <span className="card-icon-mc">MC</span>
                </div>
              </div>
              {errors.cardNumber && <span className="field-error">{errors.cardNumber}</span>}
            </div>

            {/* Expiry + CVV */}
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Expiry Date</label>
                <input
                  className={`form-input${errors.expiry ? ' input-error' : ''}`}
                  type="text"
                  name="expiry"
                  placeholder="MM/YY"
                  value={form.expiry}
                  onChange={handleChange}
                  inputMode="numeric"
                />
                {errors.expiry && <span className="field-error">{errors.expiry}</span>}
              </div>
              <div className="form-group">
                <label className="form-label">CVV</label>
                <input
                  className={`form-input${errors.cvv ? ' input-error' : ''}`}
                  type="text"
                  name="cvv"
                  placeholder="•••"
                  value={form.cvv}
                  onChange={handleChange}
                  inputMode="numeric"
                />
                {errors.cvv && <span className="field-error">{errors.cvv}</span>}
              </div>
            </div>

            {/* Submit */}
            <button
              className="btn btn-red btn-full checkout-submit"
              onClick={handleSubmit}
              disabled={loading || isInitializing || cartItems.length === 0}
            >
              {loading
                ? <span className="loading-dots"><span /><span /><span /></span>
                : `PAY $${totalPrice.toFixed(2)}`
              }
            </button>

            <p className="checkout-terms">
              By completing this purchase you agree to our{' '}
              <a href="/terms">Terms of Service</a> and{' '}
              <a href="/privacy">Privacy Policy</a>.
            </p>
          </div>
        </div>

      </div>
    </div>
  );
}
